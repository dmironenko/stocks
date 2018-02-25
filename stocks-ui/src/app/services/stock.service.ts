import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Stock} from '../model/stocks.model';
import {switchMap} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable()
export class StockService {

  private serviceUrl = environment.apiUrl + 'stocks';

  constructor(private http: HttpClient) {
  }

  public getStocksPage(pageNumber = 0, pageSize = 10): Observable<any> {
    return this.http.get(this.serviceUrl, {
      params: new HttpParams()
        .set('page', pageNumber.toString())
        .set('size', pageSize.toString())
    });
  }

  public getStockById(stockId: number): Observable<Stock> {
    return this.http.get<Stock>(this.serviceUrl + '/' + stockId);
  }

  public saveStockById(stock: Stock): Observable<Stock> {
    if (stock.id) {
      return this.http
        .put<Stock>(this.serviceUrl + '/' + stock.id, stock)
        .pipe(
          switchMap(() => this.getStockById(stock.id))
        );
    } else {
      return this.http.post<Stock>(this.serviceUrl, stock);
    }
  }

}
