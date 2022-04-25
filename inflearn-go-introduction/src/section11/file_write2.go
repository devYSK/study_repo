package main

import (
	"encoding/csv"
	"fmt"
	"os"
)

func errorCheck3(e error) {
	if e != nil {
		panic(e)
	}
}
func main() {

	file, err := os.Create("test_write.csv")
	errorCheck1(err)

	defer file.Close()

	wr := csv.NewWriter(file)

	wr.Write([]string{"kim", "4.8"})
	wr.Write([]string{"kim", "4.8"})
	wr.Write([]string{"kim", "4.8"})
	wr.Write([]string{"kim", "4.8"})
	wr.Write([]string{"kim", "4.8"})

	wr.Flush()

	fileStat, err := file.Stat()

	errorCheck3(err)

	fmt.Println(fileStat.Size()) // 파일 사이즈
	fmt.Println(fileStat.Mode()) // 파일 권한
	fmt.Println(fileStat.Name()) // 파일 이름
	fmt.Println(fileStat.ModTime()) // 파일 생성 시간
}