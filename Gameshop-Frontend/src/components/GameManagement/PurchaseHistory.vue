<script>
import { ref, onMounted } from "vue";

export default {
  name: "PurchaseHistory",
  setup() {
    const purchases = ref([]);
    const isLoading = ref(false);
    const error = ref(null);

    // Dialog State
    const isDialogOpen = ref(false);
    const selectedPurchase = ref(null);
    const refundReason = ref("");

    // Fetch purchase history
    const fetchPurchaseHistory = async () => {
      isLoading.value = true;
      try {
        const response = await fetch(
            `http://localhost:8080/customers/${localStorage.getItem('email')}/purchases`
        );
        if (!response.ok) {
          throw new Error(`Failed to fetch purchase history. Status: ${response.status}`);
        }
        purchases.value = await response.json();
        console.log(purchases.value)
      } catch (err) {
        error.value = err.message;
      } finally {
        isLoading.value = false;
      }
    };

    // Open refund dialog
    const openRefundDialog = (purchase) => {
      selectedPurchase.value = purchase;
      refundReason.value = "";
      isDialogOpen.value = true;
    };

    // Submit refund request
    const submitRefundRequest = async () => {
      if (!refundReason.value.trim()) {
        alert("Please provide a reason for the refund.");
        return;
      }

      try {
        const response = await fetch(
            `http://localhost:8080/purchases/${selectedPurchase.value.id}/refund`,
            {
              method: "POST",
              headers: {
                "Content-Type": "text/plain"
              },
              body: refundReason.value
            }
        );
        if (!response.ok) {
          throw new Error(`Refund request failed. Status: ${response.status}`);
        }
        alert("Refund request submitted successfully.");
        fetchPurchaseHistory(); // Refresh purchase history
      } catch (err) {
        alert(`Error: ${err.message}`);
      } finally {
        isDialogOpen.value = false;
      }
    };

    // Determine if the refund button should be displayed
    const canRequestRefund = (purchase) => {
      const purchaseDate = new Date(purchase.purchaseDate);
      const sevenDaysAgo = new Date();
      sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7);

      return (
          !purchase.refundRequest &&
          purchaseDate >= sevenDaysAgo
      );
    };

    // Fetch data on mount
    onMounted(fetchPurchaseHistory);

    return { purchases, isLoading, error, canRequestRefund, submitRefundRequest, isDialogOpen, refundReason, selectedPurchase, openRefundDialog };
  }
};
</script>

<template>
  <div class="purchase-history">
    <h2>Purchase History</h2>
    <div v-if="isLoading">Loading...</div>
    <div v-if="error" class="error">{{ error }}</div>
    <table v-if="purchases.length > 0" class="purchase-table">
      <thead>
      <tr>
        <th>Date</th>
        <th>Game Name</th>
        <th>Price</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(purchase, index) in purchases" :key="index">
        <td>{{ new Date(purchase.purchaseDate).toLocaleDateString() }}</td>
        <td>{{ purchase.game.name }}</td>
        <td>$ {{ purchase.purchasePrice.toFixed(2) }}</td>
        <td>
          <!-- Refund Button -->
          <button
              v-if="canRequestRefund(purchase)"
              @click="openRefundDialog(purchase)"
              class="refund-button"
          >
            Request Refund
          </button>
          <!-- Refund Status -->
          <span v-else-if="purchase.refundRequest">
              Refund Status: {{ purchase.refundRequest.status }}
            </span>
          <!-- Refund not available -->
          <span v-else>
              Refund not available
            </span>
        </td>
      </tr>
      </tbody>
    </table>
    <p v-else>No purchases found.</p>

    <!-- Refund Dialog -->
    <div v-if="isDialogOpen" class="dialog-overlay">
      <div class="dialog-box">
        <h3>Request Refund</h3>
        <p>Game: {{ selectedPurchase?.game.name }}</p>
        <textarea
            v-model="refundReason"
            placeholder="Enter your reason for requesting a refund"
            rows="4"
        ></textarea>
        <div class="dialog-actions">
          <button @click="submitRefundRequest" class="dialog-button">Submit</button>
          <button @click="isDialogOpen = false" class="dialog-button cancel">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.purchase-history {
  margin: 20px;
}

.purchase-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.purchase-table th,
.purchase-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

.purchase-table th {
  background-color: #f4f4f4;
}

.refund-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 3px;
  cursor: pointer;
}

.refund-button:hover {
  background-color: #0056b3;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.dialog-box {
  background: white;
  padding: 20px;
  border-radius: 5px;
  width: 400px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.dialog-box h3 {
  margin-top: 0;
}

.dialog-box textarea {
  width: 100%;
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.dialog-actions {
  display: flex;
  justify-content: space-between;
}

.dialog-button {
  padding: 5px 10px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.dialog-button.cancel {
  background-color: #ccc;
}

.dialog-button:hover {
  opacity: 0.9;
}
</style>