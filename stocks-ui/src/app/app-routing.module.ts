import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {StocksTableComponent} from './components/stocks-table/stocks-table.component';
import {StockItemComponent} from './components/stock-item/stock-item.component';

const appRoutes: Routes = [
  {
    path: 'stocks',
    children: [
      {
        path: '',
        pathMatch: 'full',
        component: StocksTableComponent
      },
      {
        path: 'new',
        pathMatch: 'full',
        component: StockItemComponent
      },
      {
        path: ':id',
        pathMatch: 'full',
        component: StockItemComponent
      }
    ]
  },
  {path: '**', redirectTo: '/stocks'}
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes,
      {enableTracing: true} // <-- debugging purposes only
    )
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
