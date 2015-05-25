# tiff和png分别对应300dpi图和低分辨率的示意图，后者在右框展示
# 建议通过修改png图对应的R码后，可点击update image更新图片
# 最终将tiff码对应到png码，即可在输入文件夹中得到高清图
argv <- commandArgs(TRUE)
inputfile <- argv[1]
outputfile <- argv[2]
outScaledfile <- argv[3]
library(ggplot2)
z<-read.table(inputfile,sep="\t",header=F)
names(z)<-c("Class","Ontology","Counts")
p<-ggplot(z,aes(Class,Counts))
# 最后再更新此处
tiff(file=outputfile,width=(750*4.17),height=(615*4.17),res=300)
p+facet_grid(.~Ontology,scales="free_x",space="free")+theme_bw()+geom_bar(aes(fill=Ontology,position="dodge"),stat="identity")+theme(legend.position="none",axis.text.x=element_text(angle=75,hjust=1.0,size=12))+scale_y_log10()+ylab("log10(Number of Gene Hits)")+xlab("GO term")
dev.off()
# 修改此处，具体参考ggplot2的语法
png(file=outScaledfile,width=(250*4.17),height=(205*4.17),res=100)
p+facet_grid(.~Ontology,scales="free_x",space="free")+theme_bw()+geom_bar(aes(fill=Ontology,position="dodge"),stat="identity")+theme(legend.position="none",axis.text.x=element_text(angle=75,hjust=1.0,size=12))+scale_y_log10()+ylab("log10(Number of Gene Hits)")+xlab("GO term")
dev.off()
q()