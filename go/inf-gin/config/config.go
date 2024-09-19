package config

import (
	"fmt"
	"github.com/naoina/toml"
	"log"
	"os"
	"path/filepath"
)

// 서버 설정 파일
// 강의에서는 github.com/naoina/toml 를 이용하는데 유지보수도 안되고 상당히 오래된 패키지다.. 나중에 수정해야 한다
type Config struct {
	Server struct {
		Port string
	}
}

func NewConfig(filePath string) *Config {
	// 현재 작업 디렉토리 가져오기
	dir, err := os.Getwd()
	if err != nil {
		log.Fatalln("Error getting working directory:", err)
	}

	// 경로 결합
	absPath := filepath.Join(dir, filePath)
	fmt.Println("Absolute filePath: ", absPath)

	c := new(Config)

	// 파일을 연다
	file, err := os.Open(absPath)
	if err != nil {
		log.Fatalln("config file not found:", err)
		panic(err)
	}
	defer file.Close() // 파일을 닫는다

	// TOML 파일을 디코딩
	if err = toml.NewDecoder(file).Decode(c); err != nil {
		log.Fatalln("config file NewDecoder error:", err)
		panic(err)
	}

	return c
}
