<script setup lang="ts">
import { BButton } from 'bootstrap-vue-next/dist/bootstrap-vue-next.umd';
</script>

<template>
    <div class="mainContainer">
        <div class="display-6 my-3" align="center">
            Account Settings
        </div>

        <div class="shadow p-3 mb-5 bg-body rounded"> <!-- this is for customer -->
            <BForm > 
                <div class="mb-3">
                    <BFormGroup  id="email-label" label="Email:" label-for="input-0">
                        <p class="text-secondary fst-italic" style="font-size:80%">
                            * Note that you cannot change your email address after signing up.
                        </p>
                        <BFormInput id="input-0" type="text" readonly>{{ this.email }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="username-label" label="Username:" label-for="input-1">
                        <BFormInput id="input-1" type="text" >{{ this.username }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="phoneNumber-label" label="Phone Number:" label-for="input-2">
                        <BFormInput id="input-2" type="text">{{ this.phoneNumber }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="password-label" label="Password:" label-for="input-3">
                        <BFormInput id="input-3" type="text">{{ this.password }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <Button > Add address</Button>
            </BForm>
        </div>

        <div class="shadow p-3 mb-5 bg-body rounded"> <!-- this is for employee -->
            <BForm > 
                <div class="mb-3">
                    <BFormGroup  id="email-label" label="Email:" label-for="input-0">
                        <p class="text-secondary fst-italic" style="font-size:80%">
                            * Note that you cannot change your email address after signing up.
                        </p>
                        <BFormInput id="input-0" type="text" readonly>{{ this.email }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="username-label" label="Username:" label-for="input-1">
                        <BFormInput id="input-1" type="text" >{{ this.username }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="phoneNumber-label" label="Phone Number:" label-for="input-2">
                        <BFormInput id="input-2" type="text">{{ this.phoneNumber }} </BFormInput>
                    </BFormGroup>  
                </div>  

                <div class="mb-3">
                    <BFormGroup  id="password-label" label="Password:" label-for="input-3">
                        <BFormInput id="input-3" type="text">{{ this.password }} </BFormInput>
                    </BFormGroup>  
                </div>  
            </BForm>
        </div>

    </div>
</template>