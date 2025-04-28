import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InicioComponent } from './pages/inicio/inicio.component';
import { AcercaComponent } from './pages/acerca/acerca.component';
import { ContactoComponent } from './pages/contacto/contacto.component';
import { ReactiveFormsModule } from '@angular/forms';

export const routes: Routes = [
  { path: '', component: InicioComponent },
  { path: 'acerca', component: AcercaComponent },
  { path: 'contacto', component: ContactoComponent },
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
  { path: '**', redirectTo: '' } // ruta por defecto
];
@NgModule({
    imports: [RouterModule.forRoot(routes), ReactiveFormsModule],
    exports: [RouterModule, ReactiveFormsModule]
})
export class AppRoutingModule { }
