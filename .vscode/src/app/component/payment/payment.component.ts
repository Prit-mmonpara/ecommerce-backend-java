import { Component, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

declare var Razorpay: any;

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent {
  amount: number = 0;
  orderId: string = '';
  paymentStatus: 'idle' | 'processing' | 'success' | 'failed' = 'idle';
  paymentMessage: string = '';
  showPaymentModal: boolean = false;

  constructor(private http: HttpClient, private cdr : ChangeDetectorRef) {}

  // Step 1: Create Order
  // payment.component.ts

  initiatePayment() {
    this.paymentStatus = 'processing';
    this.paymentMessage = 'Creating payment order...';

    const orderData = {
      amount: this.amount * 100, // Razorpay requires amount in paisa
      receipt: 'receipt_' + Math.floor(Math.random() * 10000),
      customerEmail: 'pritmonpara1204@gmail.com',
      productId: 1 // Make sure to set this
    };

    this.http.post('http://localhost:8080/api/payment/create', orderData).subscribe({
      next: (response: any) => {
        console.log('✅ Order Created: ', response);
        this.orderId = response.orderId;
        this.openRazorpay(response);
      },
      error: (error) => {
        console.error('❌ Error creating order:', error);
        this.paymentStatus = 'failed';
        this.paymentMessage = 'Failed to create payment order. Please try again.';
        this.showPaymentModal = true;
      }
    });
  }

  // Step 2: Open Razorpay Checkout
  openRazorpay(order: any) {
    const options = {
      key: 'rzp_test_pjipVjpCGgsb4L', // Replace with your Razorpay key
      amount: order.amount, // Amount in paisa
      currency: order.currency || 'INR',
      name: 'E-Commerce Platform',
      description: 'Test Transaction',
      order_id: order.orderId,
      handler: (response: any) => {
        console.log('🎉 Payment Success:', response);
        console.log(order);
        this.paymentStatus = 'processing';
        this.verifyPayment(response, order);
      },
      prefill: {
        name: 'Prit Monpara',
        email: 'pritmonpara1204@gmail.com',
        contact: '9773225898'
      },
      notes: {
        address: 'Corporate Office'
      },
      theme: {
        color: '#3399cc'
      }
    };

    const rzp = new Razorpay(options);
    rzp.on('payment.failed', (response: any) => {
      console.error('❌ Payment Failed:', response);
      this.paymentStatus = 'failed';
      this.paymentMessage = 'Payment failed. Please try again.';
      this.showPaymentModal = true;
    });
    rzp.open();
  }

  // Step 3: Verify Payment
  verifyPayment(response: any, order: any) {
    this.paymentStatus = 'processing';
    this.paymentMessage = 'Verifying your payment...';

    console.log('response',response);
    console.log('order', order);
    
    const paymentPayload = {
      razorpay_order_id: response.razorpay_order_id,
      razorpay_payment_id: response.razorpay_payment_id,
      razorpay_signature: response.razorpay_signature,
      amount: order.amount
    };
  
    this.http.post('http://localhost:8080/api/payment/verify', paymentPayload).subscribe({
      next: (verifyRes: any) => {
        console.log('✅ Payment verified:', verifyRes);
        this.paymentStatus = 'success';
        this.paymentMessage = 'Payment successful! Your order is being processed.';
        setTimeout(() => {
          this.showPaymentModal = true;
          this.cdr.detectChanges();
        }, 0);
        console.log('Order ID:', order.orderId);
        // After verification, send the email
        this.sendEmailAfterPayment(
          order.orderId,
          'pritmonpara12@gmail.com', // Replace with actual customer email
          order.amount
        );
        console.log('Email sent after payment verification.');
      },
      error: (error: any) => {
        console.error('❌ Payment verification failed:', error);
        this.paymentStatus = 'failed';
        this.paymentMessage = 'Payment verification failed. Please contact support.';
        this.showPaymentModal = true;
      }
    });
  }
  
  async sendEmailAfterPayment(
    orderId: string,
    email: string,
    amount: number,
  ) {
    const payload = {
      orderId: orderId,
      customerEmail: email,
      amount: amount,
    };
  
    try {
      const response = await fetch('http://localhost:8080/api/payment/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      console.log('response: ', response);
  
      if (response.ok) {
        console.log('✅ Email sent successfully.');
        this.paymentStatus = 'success';
        this.paymentMessage = 'Email sent successfully!';
      } else {
        console.error('❌ Error sending email:', await response.text());
      }
    } catch (error) {
      console.error('❌ Network Error:', error);
    }
  }

  closeModal() {
    this.showPaymentModal = false;
    // Reset status if needed
    if (this.paymentStatus === 'success' || this.paymentStatus === 'failed') {
      this.paymentStatus = 'idle';
    }
  }
}