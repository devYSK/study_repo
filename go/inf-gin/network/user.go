package network

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"inf-gin/service"
	"inf-gin/types"
	"sync"
)

var (
	userRouterInit     sync.Once // 단 한번만 실행
	userRouterInstance *userRouter
)

type userRouter struct {
	router      *Network
	userService *service.User
}

func newUserRouter(router *Network, userService *service.User) *userRouter {

	userRouterInit.Do(func() {
		userRouterInstance = &userRouter{
			router:      router,
			userService: userService,
		}

		router.registerGET("/user", userRouterInstance.get)
		router.registerPOST("/user", userRouterInstance.create)
		router.registerDELETE("/user", userRouterInstance.delete)
		router.registerUPDATE("/user", userRouterInstance.update)
	})

	return userRouterInstance
}

func (u *userRouter) create(c *gin.Context) {
	fmt.Println("post")

	var req types.CreateUserRequest

	if err := c.ShouldBindJSON(&req); err != nil {

		u.router.failedResponse(c, &types.CreateUserResponse{
			ApiResponse: types.NewApiResponse(400, "Bad Request", err.Error()),
		})

	} else if err = u.userService.Create(req.ToUser()); err != nil {
		u.router.failedResponse(c, &types.CreateUserResponse{
			ApiResponse: types.NewApiResponse(400, "Bad Request", err.Error()),
		})

		return
	} else {
		u.router.okResponse(c, &types.CreateUserResponse{
			ApiResponse: types.NewApiResponse(200, "Success", ""),
		})
	}

}

func (u *userRouter) get(c *gin.Context) {
	fmt.Println("get")

	u.userService.Get()

	u.router.okResponse(c, &types.GetUserResponse{
		ApiResponse: &types.ApiResponse{
			Result:      200,
			Description: "Success",
		},
		Users: u.userService.Get(),
	})
}

func (u *userRouter) update(c *gin.Context) {
	fmt.Println("put")

	u.userService.Update(nil)
}

func (u *userRouter) delete(c *gin.Context) {

	u.userService.Delete(nil)

	fmt.Println("delete")
}
