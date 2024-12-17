import Mock from "mockjs/dist/mock.js"

export default [
    {
        url: '/api/ads',
        method: 'post',
        response: (req) => {
            const {userCookie} = req.body;
            const adsData = Mock.mock({
                "ads|5-10": [ // 生成 5 到 10 条广告数据
                    {
                        "id|+1": 1, // 广告 ID，从 1 开始递增
                        "title": "@ctitle(5, 15)", // 广告标题，5 到 15 个字符
                        "description": "@cparagraph(2, 5)", // 广告描述，2 到 5 句话
                        "image": "@image('200x100', '#50B347', '#FFF', '广告')", // 广告图片
                        "url": "@url('http')", // 广告链接
                        "distributor": "@cname", // 广告发布者名称
                    },
                ],
            });
            if(userCookie === 123){
                return {
                    code: 200,
                    message: '获取广告申请数据成功',
                    data: adsData.data
                };
            }
            else{
                return {
                    code:400,
                    message: "Cookie Error"
                }
            }
        }
    }
]