import { Routes } from '@angular/router';
import { LoginComponent } from './modules/auth/login-component/login-component';
import { ClientsListComponent } from './modules/clients/clients-list-component/clients-list-component';
import { PricesListComponent } from './modules/prices/prices-list-component/prices-list-component';
import { ProductsListComponent } from './modules/products/products-list-component/products-list-component';
import { authGuard } from './core/guards/auth-guard';
import { UsersListComponent } from './modules/users/users-list-component/users-list-component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'clients',
    component: ClientsListComponent, 
    canActivate: [authGuard]
  },
  {
    path: 'users',
    component: UsersListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'prices',
    component: PricesListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'products',
    component: ProductsListComponent,
    canActivate: [authGuard]
  },
  { path: '', redirectTo: '/clients', pathMatch: 'full' },
  { path: '**', redirectTo: '/clients' }
];
