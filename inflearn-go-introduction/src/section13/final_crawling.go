package main

import (
	"bufio"
	"fmt"
	_"fmt"
	"net/http"
	"os"
	"strings"
	"sync"

	"github.com/yhat/scrape"
	"golang.org/x/net/html"
	"golang.org/x/net/html/atom"
)


const (
	urlRoot = "https://ruliweb.com"
	//urlRoot = "https://naver.com"
)

//에러 체크 공통 함수
func errorCheck(err error) {

	if err != nil {
		fmt.Println(err.Error())
		panic(err)
	}
}

// 첫번째 방문 페이지 대상으로 원하는 url을 파싱 후 반환하는 함수
func parseMainNodes(n *html.Node) bool {
	// must check for nil values
	if n.DataAtom == atom.A &&  n.Parent != nil {
		return scrape.Attr(n.Parent, "class") == "row"
	}

	return false
}

// 동기화를 위한 작업 그룹 선언
var wg sync.WaitGroup

func main() {
	response, err := http.Get(urlRoot)

	errorCheck(err)

	defer response.Body.Close() // 요청 Body 닫기

	root, err := html.Parse(response.Body)
	errorCheck(err)

	urlList := scrape.FindAll(root, parseMainNodes)


	for _, link := range urlList {
		//fmt.Println("Check main link : ", link, idx)

		//fmt.Println("TargetUrl :", scrape.Attr(link, "href"))

		fileName := strings.Replace(scrape.Attr(link, "href"), "https://bbs.ruliweb.com/family/", "", 1)

		//fmt.Println("fileName ", fileName)
		wg.Add(1)

		go scrapContents(scrape.Attr(link, "href"), fileName)
	}

	wg.Wait()
}

func parseNodes(n *html.Node, tag interface{}) bool {
	if n.DataAtom == tag &&  n.Parent != nil {
		return scrape.Attr(n.Parent, "class") == "row"
	}

	return false
}

func scrapContents(url string, fileName string) {
	defer wg.Done()

	response, err := http.Get(url)

	errorCheck(err)

	defer response.Body.Close()

	root, err := html.Parse(response.Body)
	errorCheck(err)


	fmt.Println(os.Getwd())

	path, _ := os.Getwd()
	fmt.Println(path)
	file, err := os.OpenFile("/scrape/" + fileName + ".txt", os.O_CREATE | os.O_RDWR, os.FileMode(0777))

	errorCheck(err)

	defer file.Close()

	w := bufio.NewWriter(file)

	//Response 데이터(html)의 원하는 부분 파싱
	matchNode := func(n *html.Node) bool {
		return n.DataAtom == atom.A && scrape.Attr(n, "class") == "deco"
	}

	for _, g := range scrape.FindAll(root, matchNode) {
		w.WriteString(scrape.Text(g) + "\r\n")
	}

	w.Flush()

}