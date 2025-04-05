import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container">
      <h1>Welcome to E-Commerce App</h1>
      <p>This is a placeholder homepage. The application is being set up.</p>
    </div>
  `,
  styles: []
})
export class HomeComponent {} 