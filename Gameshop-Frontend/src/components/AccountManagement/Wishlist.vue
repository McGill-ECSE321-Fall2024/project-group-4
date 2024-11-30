
<template>
    <div class="mainContainer">
        <div class="display-6 my-3" align="center">
            Wishlist
        </div>
        <!-- <BCard v-for="(game, index) in wishlists" :key="game.id"
        title={{ game.title }}
        img-src= "../../assets/logo.png"
        img-alt="Cover Picture"
        img-top
        tag="article"
        style="max-width: 20rem" class="wish-game">
            <BCardText>
                {{ game.description}}
            </BCardText><br>
            <BCardText>
                Price: ${{ game.price }}
            </BCardText> 
            <BCardText>
                Stock: {{ game.stock }}
            </BCardText><br>

            <BButton variant="primary" size="sm" class="add-btn" @click="addToCart(game)">Add to cart</BButton>
            <BButton size="sm" class="delete-btn" @click="removeFromWishlist(index)">Delete</BButton>

        </BCard>  -->

        <BCard
        title="Game Title"
        img-src="https://picsum.photos/600/300/?image=25"
        img-alt="Cover Picture"
        img-top
        tag="article"
        style="max-width: 20rem" class="wish-game">
            <BCardText>
                Description of the game
            </BCardText><br>
            <BCardText>
                Price: $50
            </BCardText> 
            <BCardText>
                Stock: 5
            </BCardText><br>

            <BButton size="sm" class="add-btn" @click="addToCart">Add to cart</BButton>
            <BButton size="sm" class="delete-btn" @click="removeFromWishlist(id)">Delete</BButton>

        </BCard>

        <!-- Modal -->
        <BModal id="cart-modal" ref="cartModal" title="Item Added to Cart">
        <p class="my-4">The item has been added to your cart.</p>
        <template #modal-footer="{ ok, cancel }">
            <BButton variant="secondary" @click="continueShopping">Continue Shopping</BButton>
            <BButton variant="primary" @click="goToCart">Go to Cart</BButton>
        </template>
        </BModal>

    </div>
</template>

<style scoped src="../../assets/main.css">
</style>

<script>

export default{
    data(){
        return{
            wishlists: [
                // {id:1, title: "Game 1", description: "Game 1 description", price: 50, stock: 5},
                // {id:2, title: "Game 2", description: "Game 2 description", price: 60, stock: 3},
            ],
            cart: [],
        };
    },
    methods: {
        removeFromWishlist(index){
            this.wishlists.splice(index, 1);
        },
        addToCart(game){
            this.cart.push(game);
            console.log("Added to cart!");
        },
        goToCart(){
            this.$refs.cartModal.hide();
            this.$router.push("/cart");
        },
        continueShopping(){
            this.$refs.cartModal.hide();
        }
    },
    
    
}

</script>