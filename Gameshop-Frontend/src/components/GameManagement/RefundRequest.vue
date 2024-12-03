<script>
import { ref, onMounted } from "vue";

export default {
  name: "RefundRequestReview",
  setup() {
    const refundRequests = ref([]);
    const isLoading = ref(false);
    const error = ref(null);

    // Fetch refund requests
    const fetchRefundRequests = async () => {
      isLoading.value = true;
      try {
        const response = await fetch(`http://localhost:8080/employees/${localStorage.getItem('accountId')}/refunds`);
        if (!response.ok) {
          throw new Error(`Failed to fetch refund requests. Status: ${response.status}`);
        }
        refundRequests.value = await response.json();
        console.log(refundRequests.value)
      } catch (err) {
        error.value = err.message;
      } finally {
        isLoading.value = false;
      }
    };

    // Approve refund request
    const approveRequest = async (refundRequestId) => {
      try {
        const response = await fetch(
            `http://localhost:8080/purchases/refunds/${refundRequestId}/status/approve`,
            {
              method: "POST"
            }
        );
        if (!response.ok) {
          throw new Error(`Failed to approve refund request. Status: ${response.status}`);
        }
        alert("Refund request approved successfully.");
        fetchRefundRequests(); // Refresh refund requests
      } catch (err) {
        alert(`Error: ${err.message}`);
      }
    };

    // Deny refund request
    const denyRequest = async (refundRequestId) => {
      try {
        const response = await fetch(
            `http://localhost:8080/purchases/refunds/${refundRequestId}/status/deny`,
            {
              method: "POST"
            }
        );
        if (!response.ok) {
          throw new Error(`Failed to deny refund request. Status: ${response.status}`);
        }
        alert("Refund request denied successfully.");
        fetchRefundRequests(); // Refresh refund requests
      } catch (err) {
        alert(`Error: ${err.message}`);
      }
    };

    // Fetch data on component mount
    onMounted(fetchRefundRequests);

    return { refundRequests, isLoading, error, approveRequest, denyRequest };
  }
};
</script>

<template>
  <div class="refund-review">
    <h2>Refund Requests</h2>
    <div v-if="isLoading">Loading...</div>
    <div v-if="error" class="error">{{ error }}</div>
    <table v-if="refundRequests.length > 0" class="refund-table">
      <thead>
      <tr>
        <th>User Email</th>
        <th>Purchase Date</th>
        <th>Price</th>
        <th>Game Name</th>
        <th>Reason</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(request, index) in refundRequests" :key="index">
        <td>{{ request.purchase.customer.email }}</td>
        <td>{{ new Date(request.purchase.purchaseDate).toLocaleDateString() }}</td>
        <td>$ {{ request.purchase.purchasePrice }}</td>
        <td>{{ request.purchase.game.name }}</td>
        <td>{{ request.reason }}</td>
        <td v-if="request.status === `PENDING`">
          <button @click="approveRequest(request.id)" class="approve-button">Approve</button>
          <button @click="denyRequest(request.id)" class="deny-button">Deny</button>
        </td>
        <td v-else>
          Request is {{request.status}}
        </td>
      </tr>
      </tbody>
    </table>
    <p v-else>No refund requests available.</p>
  </div>
</template>

<style scoped>
.refund-review {
  margin: 20px;
}

.refund-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.refund-table th,
.refund-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

.refund-table th {
  background-color: #f4f4f4;
}

.approve-button {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 3px;
  cursor: pointer;
}

.approve-button:hover {
  background-color: #218838;
}

.deny-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 3px;
  cursor: pointer;
}

.deny-button:hover {
  background-color: #c82333;
}

.error {
  color: red;
  margin-top: 10px;
}
</style>
