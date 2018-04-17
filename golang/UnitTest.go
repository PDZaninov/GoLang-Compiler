package main

func main() int{ 
	  result := 0 
   b := [5]int{1 , 2, 3, 4, 5} 
	  s := b[1:3] 
	  for j := 0; j < len(s); j++ { 
		result += s[j] 
	  } 
	  s = b[:3] 
	  for j := 0; j < len(s); j++ { 
		result += s[j] 
	  } 
	  s = b[3:] 
	  for j := 0; j < len(s); j++ { 
		result += s[j] 
	  } 
	  s[0] = 10 
	  for j := 0; j < len(s); j++ { 
		result += s[j] 
	  } 
	  return result 
}