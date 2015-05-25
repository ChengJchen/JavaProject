argv <- commandArgs(TRUE)
inputfile <- argv[1]
outputfile <- argv[2]
library("GO.db")
frame = read.table(inputfile,header=F)
names(frame)=c("go_id","Evidence","gene_id")
goframeData = data.frame(frame$go_id, frame$Evidence, frame$gene_id)
head(goframeData)
goFrame=GOFrame(goframeData,organism="CJ_Non_Model")
goAllFrame=GOAllFrame(goFrame)
write.table(goAllFrame@data,outputfile,quote = FALSE, sep = "\t",row.names=F)
q()