import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {
  MatButtonModule, MatFormFieldModule, MatInputModule, MatPaginatorModule, MatProgressSpinnerModule,
  MatTableModule
} from '@angular/material';
import {HttpClientModule} from '@angular/common/http';
import {AppComponent} from './app.component';
import {StocksTableComponent} from './components/stocks-table/stocks-table.component';
import {StockService} from './services/stock.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {StockItemComponent} from './components/stock-item/stock-item.component';
import {AppRoutingModule} from './app-routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';


@NgModule({
  declarations: [
    AppComponent,
    StocksTableComponent,
    StockItemComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatButtonModule,
    MatInputModule,
    MatPaginatorModule,
    MatFormFieldModule
  ],
  providers: [StockService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
