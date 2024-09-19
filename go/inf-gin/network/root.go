package network

import (
	"github.com/gin-gonic/gin"
	"inf-gin/service"
)

type Network struct {
	engin *gin.Engine

	service *service.Service
}

func NewNetwork(service *service.Service) *Network {
	r := &Network{
		engin: gin.New(),
	}

	newUserRouter(r)

	return r
}

func (n *Network) ServerStart(port string) error {
	return n.engin.Run(port)
}
