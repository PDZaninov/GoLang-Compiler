package main
			
            
			func main(){  
				a := 1  
				if a == 2 {  
					a = 2  
				} else if num := 10; num < 0 {  
					a = 3  
				} else {  
					a = 4  
				}  
			 
				i := 3  
				for i <= 5 {  
					a += 1  
					i += 1  
				}  
			 
				switch a {  
					case 7:  
						a = 100  
					case 5:  
						a = 50  
					default:  
						a = 10  
					}  
				println(a)  
			}