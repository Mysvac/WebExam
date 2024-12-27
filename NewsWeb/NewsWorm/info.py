import re
import requests
import pandas

class NewsInfo:
    def init (self):
        pass

    #1.获取响应数据
    def get_res(self,url):
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 SLBrowser/9.0.5.9101 SLBChan/105 SLBVPV/64-bit'
        }
        res = requests.get(url, headers=headers)
        res.encoding = res.apparent_encoding
        return res

    #2.解析数据
    def date_re(self,res):
        text = res.text
        news_inform1 = re.findall('<div class="news_time">(.*?)</div>', text)
        news_inform1_first_14 = news_inform1[:14]
        news_inform2 = re.findall('<div class="news_title">(.*?)</div>', text)
        news_inform2_first_14 = news_inform2[:14]
        news_inform3 = re.findall(r'<div class="news_text">\s*(.*?)\s*</div>', text, re.DOTALL)
        news_inform3_first_14 = news_inform3[:14]
        return  news_inform1_first_14,news_inform2_first_14,news_inform3_first_14

    #3.存储
    def save_csv(self,time,title,inform):

        df = pandas.DataFrame()
        df["时间"] = time
        df["标题"] = title
        df["内容"] = inform
        df.to_csv("新闻信息.csv", index=False)

    def main(self):
        time_list=[]
        title_list=[]
        inform_list=[]
        for num in range(1,11):
            page_url=f"https://www.usst.edu.cn/slyw/list{num}.htm"
            res=self.get_res(page_url)
            time,title,inform=self.date_re(res)
            time_list.extend(time)
            title_list.extend(title)
            inform_list.extend(inform)
        self.save_csv(time_list,title_list,inform_list)


if __name__ == '__main__':
    fi = NewsInfo()
    fi.main()