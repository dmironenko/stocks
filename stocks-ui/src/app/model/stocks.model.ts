export interface Stock {
  id?: number;
  lockVersion?: number;
  name: string;
  currentPrice: string;
  lastUpdate?: string;
}
