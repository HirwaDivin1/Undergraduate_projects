package com.project.gogreen.service;

import com.project.gogreen.exceptions.CartItemAlreadyExistsException;
import com.project.gogreen.exceptions.CartItemDoesNotExistsException;
import com.project.gogreen.model.cart.CartItem;
import com.project.gogreen.repo.CartItemRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CartItemService {
// Task 6: Update code for Cart Item service here
    private final CartItemRepository repo;

    public CartItemService(CartItemRepository repo){
        this.repo = repo;
    }

    public List<CartItem> getCartItems(){
        return repo.findAll();
    }

    public CartItem getCartItem(Long userId, Long productId){
        for(CartItem item: getCartItems()){
            if(item.getPk().getUser().getId() == userId && item.getPk().getProduct().getId() == productId){
                return item;
            }
        }

        throw new CartItemDoesNotExistsException("Cart item w/ user id " + userId + " and product id " + productId + " does not  exist.");

    }

    public CartItem addCartItem(CartItem cartItem){
        for (CartItem item : getCartItems()) {
            if (item.equals(cartItem)) {
                throw new CartItemAlreadyExistsException(
                        "Cart item w/ user id " + cartItem.getPk().getUser().getId() + " and product id " +
                        cartItem.getProduct().getId() + " already exists."
                );
            }
        }

        return this.repo.save(cartItem);
    }

    public CartItem updateCartItem(CartItem cartItem) {
        for (CartItem item : getCartItems()) {
            if (item.equals(cartItem)) {
                item.setQuantity(cartItem.getQuantity());
                return repo.save(item);
            }
        }

        throw new CartItemDoesNotExistsException(
                "Cart item w/ user id " + cartItem.getPk().getUser().getId() + " and product id " +
                        cartItem.getProduct().getId() + " does not exist."
        );
    }

    public void deleteCartItem (Long userId, Long productId) {
        for (CartItem item : getCartItems()) {
            if (item.getPk().getUser().getId() == userId && item.getPk().getProduct().getId() == productId) {
                repo.delete(item);
                return;
            }
        }

        throw new CartItemDoesNotExistsException(
                "Cart item w/ user id " + userId + " and product id " + productId + " does not exist."
        );
    }

}
