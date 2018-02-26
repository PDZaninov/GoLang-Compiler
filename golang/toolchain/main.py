import os

def parse(FILE_NAME):
    GO_DIRECTORY = "gofiles/"
    AST_DIRECTORY = "astfiles/"
    os.system("go build printAST.go")
    os.system("./printAST " + GO_DIRECTORY + FILE_NAME + ".go" + " > " + AST_DIRECTORY + FILE_NAME + ".ast")
    os.system("rm printAST")
CWD = os.getcwd() + "/gofiles"
for i, filename in enumerate(os.listdir(CWD)):
    parse(os.path.splitext(filename)[0])
