# Installation Instructions

To install the necessary dependencies, follow these steps:

1. Make sure you have Node.js and npm installed on your system.

2. Navigate to the frontend directory:
   ```
   cd frontend
   ```

3. Install the dependencies:
   ```
   npm install
   ```

4. Start the development server:
   ```
   npm start
   ```

5. Open your browser and go to http://localhost:4200

## Common Issues

If you're experiencing linter errors about missing modules like `@angular/core`, `@angular/common`, etc., it's usually because the dependencies haven't been installed yet. Run `npm install` to fix this.

Error messages like "This syntax requires an imported helper but module 'tslib' cannot be found" will also be resolved after installing the dependencies.

## Building for Production

To build the application for production:

```
npm run build
```

The build artifacts will be stored in the `dist/` directory.
