package service

import (
	"inf-gin/repository"
	"sync"
)

var (
	serviceInit    sync.Once
	serverInstance *Service
)

type Service struct {
	repository *repository.Repository

	User *User
}

func NewService(rep *repository.Repository) *Service {

	serviceInit.Do(func() {
		serverInstance = &Service{
			repository: rep,
		}

		serverInstance.User = newUserService(rep.UserRepository)
	})

	return serverInstance
}
