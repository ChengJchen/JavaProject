# topGOGOenrich.r
argv <- commandArgs(TRUE)
topGOData <- argv[1]
universeData <- argv[2]
testSetData <- argv[3]
outpath <- argv[4]
# Must install the following R package before you conducting enrichment
# 'cause b3go invokes them
library(topGO)
geneID2GO <- readMappings(file = topGOData)
################################
# topGO use the All Gene which have GO annotation as Univese)
##################################
## Just for heitance from other java class
# universe<-read.table(universeData,header=F)
geneNames <- names(geneID2GO)
myInterestingGenes = read.table(testSetData)
myInterestingGenes<-unlist(as.vector(myInterestingGenes))
# str(myInterestingGenes)
geneList <- factor(as.integer(geneNames %in% myInterestingGenes))
names(geneList)<-geneNames

##################################
# May change the directoy below to set output 
# Directory; default is same with input table
##################################
setwd(outpath)
##################################
# Params for Molecular Function erichment; do erichment
##################################
GOdata <- new("topGOdata", ontology = "MF", allGenes = geneList,annot = annFUN.gene2GO, gene2GO = geneID2GO)
# Fisher
test.stat <- new("classicCount", testStatistic = GOFisherTest, name = "Fisher test")
resultFisher <- getSigGroups(GOdata, test.stat)
pvalFis <- score(resultFisher)
# p-values distribution --- don't want to draw it here
# hist(pvalFis, 50, xlab = "p-values")
printGraph(GOdata, resultFisher, firstSigNodes = 10, fn.prefix = "MF_tGO", useInfo = "all", pdfSW = TRUE)
# output resultFisher
allRes <- GenTable(GOdata, classic = resultFisher, ranksOf = "classic", topNodes = 500)
write.csv(file="MF_tGO.csv",allRes)

##################################
# Params for CC erichment; do erichment
##################################
GOdata <- new("topGOdata", ontology = "CC", allGenes = geneList,annot = annFUN.gene2GO, gene2GO = geneID2GO)
# Fisher
test.stat <- new("classicCount", testStatistic = GOFisherTest, name = "Fisher test")
resultFisher <- getSigGroups(GOdata, test.stat)
pvalFis <- score(resultFisher)
# p-values distribution --- don't want to draw it here
# hist(pvalFis, 50, xlab = "p-values")
printGraph(GOdata, resultFisher, firstSigNodes = 10, fn.prefix = "CC_tGO", useInfo = "all", pdfSW = TRUE)
# output resultFisher
allRes <- GenTable(GOdata, classic = resultFisher, ranksOf = "classic", topNodes = 500)
write.csv(file="CC_tGO.csv",allRes)
##################################
# Params for BP erichment; do erichment
##################################
GOdata <- new("topGOdata", ontology = "BP", allGenes = geneList,annot = annFUN.gene2GO, gene2GO = geneID2GO)
# Fisher
test.stat <- new("classicCount", testStatistic = GOFisherTest, name = "Fisher test")
resultFisher <- getSigGroups(GOdata, test.stat)
pvalFis <- score(resultFisher)
# p-values distribution --- don't want to draw it here
# hist(pvalFis, 50, xlab = "p-values")
printGraph(GOdata, resultFisher, firstSigNodes = 10, fn.prefix = "BP_tGO", useInfo = "all", pdfSW = TRUE)
# output resultFisher
allRes <- GenTable(GOdata, classic = resultFisher, ranksOf = "classic", topNodes = 500)
write.csv(file="BP_tGO.csv",allRes)
q()
