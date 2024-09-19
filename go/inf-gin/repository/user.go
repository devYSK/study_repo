package repository

import "inf-gin/types"

type UserRepository struct {
	UserMap []*types.User
}

func NewUserRepository() *UserRepository {
	return &UserRepository{
		UserMap: []*types.User{},
	}
}

func (r *UserRepository) Create(user *types.User) error {
	r.UserMap = append(r.UserMap, user)
	return nil
}

func (r *UserRepository) Update(user *types.User) error {

	return nil
}

func (r *UserRepository) Delete(user *types.User) error {
	return nil
}

func (r *UserRepository) Get() []*types.User {
	return r.UserMap
}
