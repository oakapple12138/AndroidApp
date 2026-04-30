#ifndef OAK_HPP_
#define OAK_HPP_
#include<string>
#include <filesystem>
#include <algorithm>
#include <vector>
#include <utility>

namespace oak
{
    template<typename T>std::string operator+(const std::string&str,const T&t){return str + std::to_string(t);}
    template<typename T>std::string operator+(const T&t,const std::string&str) { return std::to_string(t)+str;}


    std::pair<std::vector<std::filesystem::path>,std::vector<std::filesystem::path>>文件遍历
    (const std::filesystem::path& 源文件夹,std::filesystem::directory_options 模式=std::filesystem::directory_options::skip_permission_denied)
    {
        using namespace std::filesystem;
        std::vector<path> 文件夹列表,文件列表;
        for (const auto &子文件: recursive_directory_iterator(源文件夹,模式))
        {
            if (子文件.is_directory()) 文件夹列表.push_back(子文件.path());
            else 文件列表.push_back(子文件.path());
        }

        return {文件夹列表, 文件列表};
    }



    std::vector<std::string> 文件夹复制(const std::string& 源路径, const std::string& 目标路径)
    {
        using namespace std::filesystem;

        path 源文件夹(源路径);
        path 目标文件夹(目标路径);

        std::vector<std::string> 失败列表;

        auto [文件夹列表,文件列表]=文件遍历(源文件夹);

        auto depth_count = [&](const path &p)
        {
            std::string rel = relative(p, 源文件夹).generic_string();
            return (int) std::count(rel.begin(), rel.end(), '/');
        };

        std::sort(文件夹列表.begin(), 文件夹列表.end(), [&](const path &a, const path &b)
        {return depth_count(a) < depth_count(b);});

        create_directories(目标文件夹);

        for (const auto& dir_path : 文件夹列表)
        {
            path 相对路径 = 目标文件夹 / relative(dir_path, 源文件夹);
            create_directories(相对路径);
        }

        for (const auto& file_path : 文件列表)
        {
            path 相对路径 = 目标文件夹 / relative(file_path, 源文件夹);
            try {
                create_directories(相对路径.parent_path());
            } catch (const std::exception&)
            {
                失败列表.push_back(file_path.string());
                continue;
            }

            try {
                copy_file(file_path, 相对路径, copy_options::overwrite_existing);
            } catch (const std::exception&)
            {
                失败列表.push_back(file_path.string());
                continue;
            }
        }

        return 失败列表;
    }
}




#endif // OAK_HPP_
