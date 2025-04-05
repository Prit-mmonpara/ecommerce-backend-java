import { Component } from '@angular/core';
import { PaymentComponent } from './component/payment/payment.component';
import { ProductsViewComponent } from './component/products-view/products-view.component';
@Component({
  selector: 'app-root',
  imports: [PaymentComponent, ProductsViewComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'razorpay-integration';
}
