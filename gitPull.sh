#!/bin/sh
git stash
git pull origin master
git stash apply
