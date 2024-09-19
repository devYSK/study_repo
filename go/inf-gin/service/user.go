package service

import (
	"inf-gin/repository"
	"inf-gin/types"
)

type User struct {
	userRepository *repository.UserRepository
}

func newUserService(userRepository *repository.UserRepository) *User {
	return &User{
		userRepository: userRepository,
	}
}

func (u *User) Create(user *types.User) error {
	return u.userRepository.Create(user)
}

func (u *User) Update(user *types.User) error {
	return u.userRepository.Update(user)
}

func (u *User) Delete(user *types.User) error {
	return u.userRepository.Delete(user)
}

func (u *User) Get() []*types.User {
	return u.userRepository.Get()
}
