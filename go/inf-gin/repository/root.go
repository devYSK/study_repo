package repository

import (
	"sync"
)

var (
	repositoryInit     sync.Once
	repositoryInstance *Repository
)

type Repository struct {
	UserRepository *UserRepository
}

func NewRepository() *Repository {

	repositoryInit.Do(func() {
		repositoryInstance = &Repository{}

		repositoryInstance.UserRepository = NewUserRepository()
	})

	return repositoryInstance
}
