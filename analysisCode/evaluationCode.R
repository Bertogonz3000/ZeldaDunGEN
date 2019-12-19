#Creates a heat map with the linearity and leniency scores of all the dungeons

#Read in data cvs format with three columns: names, x coordinate of the point, y coordinate of the point
mydata <- read.table ("converted.csv", header = TRUE,sep="," )

#keeps track of the start of the rows corresponding to dungeons in order
listOfDungeonsStart<- integer()

#keeps track of the end of the rows corresponding to dungeons in order
listOfDungeonsEnd <- integer()

#this forloop goes through and determined which rows correspond to individual dungeons
#stores this information in listOfDungeonsStart and listOfDungeonsEnd
start <-0
end <-0
numDungeons <- 0
for (i in 1:dim(mydata)) {
  if (mydata[i,1] < 0) {
    newEnd <- i - 1
    end <- newEnd
    listOfDungeonsStart[numDungeons]<-start
    listOfDungeonsEnd[numDungeons]<-newEnd 
    start <- i + 1
    number <- numDungeons
    numDungeons <- number + 1
  }
}

linearityList <- list()
leniencyList <- list()

for (i in 1:length(listOfDungeonsStart)) {
  #get when this particular dungeon starts and ends in the file
  begin <- listOfDungeonsStart[i]
  done <- listOfDungeonsEnd[i]
  
  # extract x and y values
  x<-mydata[begin:done,2]
  y<-mydata[begin:done,3]
  
  #do the regression analysis
  relation <- lm(y~x)
  
  #plot the regression analysis
  plot(y,x,col = "red",main = "Regression",
       abline(lm(x~y)),cex = 1.3,pch = 16,xlab = "x",ylab = "y")
  #number of x rows
  datasize <- dim(mydata)
  
  #then score each level by taking the sum of the absolute values of the distance
  #from each platform midpoint to its expected value on the line and divide by the 
  #total number of points
  error<- 0
  counter <- 0 
  for (k in begin:done) {
    thisTurn <- counter  + 1
    counter <- thisTurn
  
    #predict what the y value should have been according to the line
    result <- predict(relation)
  
    #get the difference between the y values
    distance<- abs(result[thisTurn] - mydata[k,3])
    
    #add up all the distances
    past<-error
    error<-past + distance
  }
  
  #divide total error by the number of points
  size <- done - begin + 1
  linearityScore<- (error/ size) 
 
  #the number of monsters information
  numberOfMonsters <- mydata[begin:done,4]
  #the miniboss information
  miniboss <- mydata[begin:done,5]
  
  totalScore <- 0
  totalMiniBoss <- 0 
  
  #Get the average number of monsters per room and add the number of mini bosses
  for (l in begin:done) {
    thisMonsterNumber<-mydata[l,4]
    minibossNumber <- mydata[l,5]
    deadConcentration<- thisMonsterNumber 
    past<- totalScore
    totalScore<- past + deadConcentration
    pastMini <- totalMiniBoss
    totalMiniBoss <- pastMini + minibossNumber
   
    
  }
  leniencyScore<-(totalScore/length(mydata)) + minibossNumber

  
  linearityList[i]<- linearityScore
  leniencyList[i] <- leniencyScore
  
  
}

getwd()

#the location of where all the files will be created and read
setwd("/Users/irenazracoskun/Desktop")

#Put the linearity and leniency in the correct format to be able to do the hexbin plot
write.csv(linearityList, file = "scoreResults1.csv")
write.csv(leniencyList, file = "scoreResults2.csv")

mydata1 <- read.table ("scoreResults1.csv", header = TRUE,sep="," )
mydata2 <- read.table ("scoreResults2.csv", header = TRUE,sep="," )

linearity<- mydata1[1,]
leniency <- mydata2 [1,]


#the hexagon heatmap
library(hexbin)

#make the hexbin calculation
thehexbin <- hexbin(linearity, leniency, xbins = 49, shape = 1,
       xbnds = range(linearity), ybnds = range(leniency),
       xlab = NULL, ylab = NULL, IDs = FALSE)

plot(thehexbin)












