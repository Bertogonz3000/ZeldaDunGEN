#Merges the input from the dungeon generator

getwd()

#The location of the dungeon files that are created
setwd("/Users/irenazracoskun/Desktop/ZeldaDunGEN/dungeons/analysis")

#get all the csv files
temp = list.files(pattern = "*.csv")

#make a data frame from all of them
myFiles = lapply(temp, read.delim)

#location to create the merged file 
setwd("/Users/irenazracoskun/Desktop")

#merge all of them
hope <- Reduce(rbind, myFiles)

write.csv(hope, file = "converted.csv")


