# E-Commerce Frontend

This is a modern Angular frontend for an e-commerce application with role-based authentication.

## Features

- Standalone component architecture
- Role-based authentication with JWT
- User registration and login
- Product catalog with categories
- Shopping cart functionality
- Watchlist for saving favorite products
- Order processing and checkout
- Admin dashboard with product management
- Responsive design with Bootstrap 5

## Prerequisites

- Node.js (v16 or later)
- npm (v8 or later)
- Angular CLI: `npm install -g @angular/cli`

## Getting Started

1. Clone the repository

2. Navigate to the frontend directory:
   ```
   cd frontend
   ```

3. Install dependencies:
   ```
   npm install
   ```

4. Start the development server:
   ```
   ng serve
   ```

5. Open your browser and navigate to `http://localhost:4200`

## Building for Production

1. Build the application:
   ```
   ng build --configuration production
   ```

2. The built artifacts will be stored in the `dist/` directory.

## Hosting Instructions

### Hosting on Firebase

1. Install the Firebase CLI:
   ```
   npm install -g firebase-tools
   ```

2. Login to Firebase:
   ```
   firebase login
   ```

3. Initialize your project:
   ```
   firebase init
   ```
   - Select "Hosting" when prompted
   - Choose your Firebase project
   - Specify "dist/ecommerce-frontend" as the public directory
   - Configure as a single-page app (SPA)
   - Do not overwrite index.html

4. Deploy to Firebase:
   ```
   firebase deploy
   ```

### Hosting on Netlify

1. Build your application:
   ```
   ng build --configuration production
   ```

2. Deploy to Netlify:
   - Go to [Netlify](https://app.netlify.com/)
   - Create an account or sign in
   - Drag and drop the `dist/ecommerce-frontend` folder to the Netlify dashboard
   - Alternatively, connect your GitHub repository and set up continuous deployment

### Hosting on Vercel

1. Install the Vercel CLI:
   ```
   npm install -g vercel
   ```

2. Deploy to Vercel:
   ```
   vercel
   ```
   - Follow the prompts to set up your project

## Connecting to the Backend

The application is configured to connect to a backend at `http://localhost:8080`. To change this:

1. Update the API URL in the environment files:
   - For development: `src/environments/environment.ts`
   - For production: `src/environments/environment.prod.ts`

## Testing Users

- Regular User:
  - Username: user
  - Password: password

- Admin User:
  - Username: admin
  - Password: password

## License

This project is licensed under the MIT License. 