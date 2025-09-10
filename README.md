# CodeAlpha-JavaProgramming-task-2
this is my intership task no 2.


# StockTradingCSV

**One-line:** Java console stock-trading simulator (buy/sell, market tick, save/load to `portfolio.csv`).

## Description

A simple Java console application that simulates a tiny stock trading platform. You can view market prices, buy and sell stocks, simulate price changes, and save/load your portfolio to a CSV file.

## Features

* Show current market prices
* Buy and sell stocks
* Simulate market ticks (random price updates)
* Save and load portfolio to `portfolio.csv`

## How to run

1. Compile:

```bash
javac StockTradingCSV.java
```

2. Run:

```bash
java StockTradingCSV
```

## Files

* `StockTradingCSV.java` — main source file
* `portfolio.csv` — saved portfolio (created after saving)

## Notes

* Uses simple CSV format: first line `username,cash` then each holding on its own line as `SYMBOL,quantity`.
* Starting cash default is ₹50,000 (can be changed in code).

## License

MIT


