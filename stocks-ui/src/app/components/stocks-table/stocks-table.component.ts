import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {StockService} from '../../services/stock.service';
import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {Observable} from 'rxjs/Observable';
import {Stock} from '../../model/stocks.model';
import {MatPaginator} from '@angular/material';
import {catchError, finalize, tap} from 'rxjs/operators';
import {of} from 'rxjs/observable/of';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import { Router } from '@angular/router';

@Component({
  selector: 'app-stockstable',
  templateUrl: './stocks-table.component.html',
  styleUrls: ['./stocks-table.component.css']
})
export class StocksTableComponent implements AfterViewInit, OnInit {

  displayedColumns = ['id', 'name', 'currentPrice', 'lastUpdate'];

  datasource = new UserDataSource(this.stockService);

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private stockService: StockService, private router: Router) {
  }

  ngOnInit() {
    this.datasource.loadStocks(0);
  }

  ngAfterViewInit() {
    this.paginator.page.pipe(tap(() => this.loadPage()))
      .subscribe();
  }

  loadPage() {
    this.datasource.loadStocks(
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }

  rowClicked(row: Stock) {
    this.router.navigate(['/stocks/' + row.id]);
  }
}

export class UserDataSource implements DataSource<Stock> {
  private stocksSubject = new BehaviorSubject<Stock[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();
  public totalPages;

  constructor(private stockService: StockService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<Stock[]> {
    return this.stocksSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.stocksSubject.complete();
    this.loadingSubject.complete();
  }

  loadStocks(pageIndex = 0, pageSize = 10) {

    this.loadingSubject.next(true);

    this.stockService.getStocksPage(pageIndex, pageSize).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    ).subscribe(response => {
      this.totalPages = response['totalElements'];
      this.stocksSubject.next(response['content']);
    });
  }
}
