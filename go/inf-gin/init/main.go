package main

import (
	"flag"
	"fmt"
	"inf-gin/init/cmd"
	"net/http"
)

var configPathFlag = flag.String("config", "./config.toml", "config file not found")

func main() {

	flag.Parse()
	config := cmd.NewCmd(*configPathFlag)

	fmt.Println(config)

	//http.HandleFunc("/", helloWord)
	//
	//if err := http.ListenAndServe(":8080", nil); err != nil {
	//	fmt.Println(err)
	//	panic(err)
	//}

}

func helloWord(w http.ResponseWriter, r *http.Request) {
	fmt.Println("Hello World")
}
