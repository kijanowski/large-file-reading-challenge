#!/bin/bash

while true
do
  for city in Kraków Łódź Warszawa
  do
    for year in 2000 2001 2002
    do
      echo "$city;$year-01-01 00:00:00.000;$((RANDOM%9)).$((RANDOM%99))" >> temps.csv
    done
  done

done
