import os

def parse(FILE_NAME):
    GO_DIRECTORY = "gofiles/"
    AST_DIRECTORY = "astfiles/"
    os.system("go build printAST.go")
    os.system("./printAST " + GO_DIRECTORY + FILE_NAME + ".go" + " > " + AST_DIRECTORY + FILE_NAME + ".ast")
    os.system("rm printAST")
    

CWD = os.getcwd() + "/gofiles"
for filename in os.listdir(CWD):
    SHORTENED_FILENAME = os.path.splitext(filename)[0]
    print("Making AST for " + SHORTENED_FILENAME + " ...")
    parse(SHORTENED_FILENAME)
for filename in os.listdir(os.getcwd() + "/astfiles"):
    print("Running ./gt on file " + filename)
    DIRECTORY = "/astfiles"
    os.system("." + DIRECTORY + "/gt " + os.getcwd() + DIRECTORY +"/" + filename)
