<script>
import { ref, onMounted } from "vue";

export default {
  name: "PurchaseHistory",
  setup(props) {
    const purchases = ref([]);
    const isLoading = ref(false);
    const error = ref(null);

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

    // Submit refund request
    const requestRefund = async (purchaseId) => {
      try {
        const response = await fetch(
            `http://localhost:8080/customers/${localStorage.getItem('email')}/purchases/${purchaseId}/refund`,
            {
              method: "POST"
            }
        );
        if (!response.ok) {
          throw new Error(`Refund request failed. Status: ${response.status}`);
        }
        alert("Refund request submitted successfully.");
        fetchPurchaseHistory(); // Refresh the purchase history to reflect the new refund status
      } catch (err) {
        alert(`Error: ${err.message}`);
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

    return { purchases, isLoading, error, canRequestRefund, requestRefund };
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
              @click="requestRefund(purchase.id)"
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

.error {
  color: red;
  margin-top: 10px;
}
</style>
