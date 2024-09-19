package cmd

import (
	"inf-gin/config"
	"inf-gin/network"
	"inf-gin/repository"
	"inf-gin/service"
)

type Cmd struct {
	config     *config.Config
	network    *network.Network
	repository *repository.Repository
	service    *service.Service
}

func NewCmd(filePath string) *Cmd {
	c := &Cmd{
		config: config.NewConfig(filePath),
	}

	c.repository = repository.NewRepository()
	c.service = service.NewService(c.repository)
	c.network = network.NewNetwork(c.service)

	err := c.network.ServerStart(c.config.Server.Port)

	if err != nil {
		panic(err)
	}

	return c
}
