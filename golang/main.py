import os

def parse(FILE_NAME):
    GO_DIRECTORY = "gofiles/"
    AST_DIRECTORY = "astfiles/"
    os.system("go build printAST.go")
    os.system("./printAST " + GO_DIRECTORY + FILE_NAME + ".go" + " > " + AST_DIRECTORY + FILE_NAME + ".ast")
    os.system("rm printAST")
    

CWD = os.getcwd() + "/gofiles"
print (CWD)
for filename in os.listdir(CWD):
    SHORTENED_FILENAME = os.path.splitext(filename)[0]
    parse(SHORTENED_FILENAME)
for filename in os.listdir(os.getcwd() + "/astfiles"):
    os.system("./astfiles/gt " + os.getcwd() + "/" + filename)
