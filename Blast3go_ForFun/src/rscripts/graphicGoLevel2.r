# tiff��png�ֱ��Ӧ300dpiͼ�͵ͷֱ��ʵ�ʾ��ͼ���������ҿ�չʾ
# ����ͨ���޸�pngͼ��Ӧ��R��󣬿ɵ��update image����ͼƬ
# ���ս�tiff���Ӧ��png�룬�����������ļ����еõ�����ͼ
argv <- commandArgs(TRUE)
inputfile <- argv[1]
outputfile <- argv[2]
outScaledfile <- argv[3]
library(ggplot2)
z<-read.table(inputfile,sep="\t",header=F)
names(z)<-c("Class","Ontology","Counts")
p<-ggplot(z,aes(Class,Counts))
# ����ٸ��´˴�
tiff(file=outputfile,width=(750*4.17),height=(615*4.17),res=300)
p+facet_grid(.~Ontology,scales="free_x",space="free")+theme_bw()+geom_bar(aes(fill=Ontology,position="dodge"),stat="identity")+theme(legend.position="none",axis.text.x=element_text(angle=75,hjust=1.0,size=12))+scale_y_log10()+ylab("log10(Number of Gene Hits)")+xlab("GO term")
dev.off()
# �޸Ĵ˴�������ο�ggplot2���﷨
png(file=outScaledfile,width=(250*4.17),height=(205*4.17),res=100)
p+facet_grid(.~Ontology,scales="free_x",space="free")+theme_bw()+geom_bar(aes(fill=Ontology,position="dodge"),stat="identity")+theme(legend.position="none",axis.text.x=element_text(angle=75,hjust=1.0,size=12))+scale_y_log10()+ylab("log10(Number of Gene Hits)")+xlab("GO term")
dev.off()
q()