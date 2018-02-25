#!/bin/bash
set -e

npm install
ng build --prod --env=prod
docker build -t dmironenko/stocks-ui:1.0.0 .
