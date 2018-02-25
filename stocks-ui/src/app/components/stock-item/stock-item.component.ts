import {Component, OnDestroy, OnInit} from '@angular/core';
import {StockService} from '../../services/stock.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormControl, Validators} from '@angular/forms';
import {Subscription} from 'rxjs/Subscription';
import {Stock} from '../../model/stocks.model';
import {switchMap} from 'rxjs/operators';
import {of} from 'rxjs/observable/of';

@Component({
  selector: 'app-stock-item',
  templateUrl: './stock-item.component.html',
  styleUrls: ['./stock-item.component.css']
})
export class StockItemComponent implements OnInit, OnDestroy {
  private sub: Subscription;
  public stock: Stock;
  public nameFormControl = new FormControl('', [Validators.required]);
  public priceFormControl = new FormControl(
    '',
    [Validators.required, Validators.pattern('[0-9]+\\.?[0-9]*')]
  );
  public saveFailed: boolean;

  title = 'Stocks';

  constructor(private stockService: StockService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {
    this.sub = this.route.params
      .pipe(
        switchMap(routeParams => {
          const stockItemId = routeParams.id;
          if (stockItemId) {
            return this.stockService.getStockById(stockItemId);
          } else {
            const defaultStock: Stock = {
              name: '',
              currentPrice: ''
            };
            return of(defaultStock);
          }
        })
      )
      .subscribe((stock: Stock) => {
        this.stock = stock;
        this.nameFormControl.setValue(stock.name);
        this.priceFormControl.setValue(stock.currentPrice);
      });
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  public saveForm() {
    this.stock.name = this.nameFormControl.value;
    this.stock.currentPrice = this.priceFormControl.value;
    this.stockService.saveStockById(this.stock)
      .subscribe(stock => {
        this.stock = stock;
        this.router.navigate(['/stocks/', stock.id]);
      }, error => this.saveFailed = true);
  }

}
