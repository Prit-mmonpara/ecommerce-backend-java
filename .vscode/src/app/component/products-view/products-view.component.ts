import { Component } from '@angular/core';
import { ProductService } from '../../service/product.service';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Dialog } from 'primeng/dialog';

@Component({
  selector: 'app-products-view',
  standalone: true,
  imports: [ButtonModule, CardModule, FormsModule, CommonModule, Dialog],
  templateUrl: './products-view.component.html',
  styleUrls: ['./products-view.component.css'],
})
export class ProductsViewComponent {
  products: any[] = []; // Product list array
  ProductDialog: boolean = false; // Control dialog visibility
  visible: boolean = false; // Visibility flag for dialog
  newProduct: any = {
    name: '',
    description: '',
    price: null,
    stock: null,
    category: { id: null },
  }; // New product model

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.fetchProducts(); // Load products on component load
  }

  // Get Products
  fetchProducts() {
    this.productService.getProducts().subscribe({
      next: (res) => {
        console.log('✅ Products fetched:', res);
        this.products = res;
      },
      error: (err) => {
        console.error('❌ Error fetching products:', err);
      },
    });
  }

  // Show Dialog for Adding New Product
  showDialogProduct() {
    this.ProductDialog = true;
    this.visible = true;
    this.resetNewProduct(); // Clear form fields before opening
  }

  // Reset Product Form Fields
  resetNewProduct() {
    this.newProduct = {
      name: '',
      description: '',
      price: null,
      stock: null,
      category: { id: null },
    };
  }

  // Add New Product
  addNewProduct() {
    // Validate Form Data
    if (!this.newProduct.name || !this.newProduct.description || !this.newProduct.price || !this.newProduct.stock) {
      alert('Please fill all the fields before submitting! 😕');
      return;
    }

    this.productService.createProduct(this.newProduct).subscribe({
      next: (res) => {
        console.log('✅ Product created:', res);
        this.fetchProducts(); // Refresh product list after adding
        this.closeDialog(); // Close dialog after success
      },
      error: (err) => {
        console.error('❌ Error creating product:', err);
      },
    });
  }

  // Close Dialog
  closeDialog() {
    this.ProductDialog = false; // Correct control
    this.resetNewProduct(); // Reset values after closing
  }

  // Load More Products
  loadMore() {
    this.productService.getProducts().subscribe({
      next: (res) => {
        console.log('✅ More products loaded:', res);
        this.products = [...this.products, ...res]; // Append more products
      },
      error: (err) => {
        console.error('❌ Error loading more products:', err);
      },
    });
  }
}
