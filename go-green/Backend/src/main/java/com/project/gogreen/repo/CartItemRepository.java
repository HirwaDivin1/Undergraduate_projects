package com.project.gogreen.repo;

import com.project.gogreen.model.cart.CartItem;
import com.project.gogreen.model.cart.CartItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

// Task 3: Write your code here
public interface CartItemRepository extends JpaRepository <CartItem, CartItemPK>{

}