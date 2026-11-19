import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

// Inicia a aplicação Angular usando a configuração principal.
bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
