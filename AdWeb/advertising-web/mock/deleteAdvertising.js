import Mock from "mockjs/dist/mock.js";

export default [
    {
        url: '/api/delete-advertising', // 接口 URL
        method: 'post', // 请求方法
        response: (req) => {
            const {id} = req.body;

            // 模拟删除逻辑
            if (id) {
                // 模拟删除成功
                return {
                    code: 200,
                    message: '广告删除成功',
                    data: {
                        id,
                    },
                };
            } else {
                // 模拟删除失败
                return {
                    code: 400,
                    message: '广告删除失败，id 不能为空',
                    data: null,
                };
            }
        }
    }
];