argv <- commandArgs(TRUE)
gostatData <- argv[1]
universeData <- argv[2]
testSetData <- argv[3]
outpath <- argv[4]
# Must install the following R package before you conducting enrichment
# 'cause b3go invokes them
library("AnnotationForge")
library("GSEABase")
library("GOstats")
frame = read.table(gostatData,header=F)
################################
# universe ids were read from the inputdata 
# (Some people just want to use the All Gene which have GO annotation as Univese)
# Because that which strategy is better is still be in hot debate,
# I don't want to make this function now.
# For those who want to change the background for enrichment,
# now you should output the B3G current table,
# write a script to filter all gene that do not contain GO annotation,
# and then load the filtered table back in B3G, do Erichment step.
##################################
universe<-read.table(universeData,header=F)
genes = read.table(testSetData,header=F)
names(frame)=c("go_id","Evidence","gene_id")
goframeData = data.frame(frame$go_id, frame$Evidence, frame$gene_id)
goFrame=GOFrame(goframeData,organism="Non_model_CJ")
goAllFrame=GOAllFrame(goFrame)
gsc <- GeneSetCollection(goAllFrame, setType = GOCollection())
universe=as.vector(levels(universe$V1))
genes<-unlist(genes)
genes<-as.vector(genes)
##################################
# May change the directoy below to set output Directory; default is same with input table
##################################
setwd(outpath)
##################################
# Params for Molecular Function erichment; do erichment
##################################
params <- GSEAGOHyperGParams(name="My Custom GSEA based annot Params",geneSetCollection=gsc,geneIds = genes,universeGeneIds = universe,ontology = "MF",pvalueCutoff = 0.05,conditional = FALSE,testDirection = "over")
Over <- hyperGTest(params)
htmlReport(Over,file='Non_model_GO_enrichment_MF.html')
##################################
# Params for CC erichment; do erichment
##################################
params <- GSEAGOHyperGParams(name="My Custom GSEA based annot Params",geneSetCollection=gsc,geneIds = genes,universeGeneIds = universe,ontology = "CC",pvalueCutoff = 0.05,conditional = FALSE,testDirection = "over")
Over <- hyperGTest(params)
htmlReport(Over,file='Non_model_GO_enrichment_CC.html')
##################################
# Params for BP erichment; do erichment
##################################
params <- GSEAGOHyperGParams(name="My Custom GSEA based annot Params",geneSetCollection=gsc,geneIds = genes,universeGeneIds = universe,ontology = "BP",pvalueCutoff = 0.05,conditional = FALSE,testDirection = "over")
Over <- hyperGTest(params)
htmlReport(Over,file='Non_model_GO_enrichment_BP.html')
q()
