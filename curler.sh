#!/bin/bash

while true
do
  for city in Kraków Łódź Warszawa
  do
    curl http://localhost:8000/$city | json_pp &
  done
  curl http://localhost:8000 | json_pp &
  curl http://localhost:8000/Siedlce | json_pp &
  sleep 1

done
