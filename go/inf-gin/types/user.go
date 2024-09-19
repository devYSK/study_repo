package types

type User struct {
	Name string `json:"name"`
	Age  int64  `json:"age"`
}

type UserResponse struct {
	*ApiResponse
	*User
}

type GetUserResponse struct {
	*ApiResponse
	Users []*User `json:"users"`
}

type CreateUserRequest struct {
	Name string `json:"name" binding:"required"`
	Age  int64  `json:"age" binding:"required"`
}

func (c *CreateUserRequest) ToUser() *User {
	return &User{
		Name: c.Name,
		Age:  c.Age,
	}
}

type CreateUserResponse struct {
	*ApiResponse
}

type UpdateUserRequest struct {
	Name string `json:"name" bindi3ng:"required"`

	UpdatedAge int64 `json:"updatedAge" binding:"required"`
}

type UpdateUserResponse struct {
	*ApiResponse
}

type DeleteUserRequest struct {
	Name string `json:"name" binding:"required"`
}

type DeleteUserResponse struct {
	*ApiResponse
}
