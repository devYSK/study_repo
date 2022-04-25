package main

import (
	"fmt"
	"github.com/tealeg/xlsx"
)

func main() {

	xfile := "sample.xlsx"

	xlFile, err := xlsx.OpenFile(xfile)

	if err != nil {
		panic("Excel Loads Error")
	}

	for _, sheet := range xlFile.Sheets {
		for _, row := range sheet.Rows {
			for _, cell := range row.Cells {
				text := cell.String()

				fmt.Printf("%s\t", text)
			}
			fmt.Println()
		}
	}
}