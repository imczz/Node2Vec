function [ Result ] = analyse( Matrix, shape)
%UNTITLED 此处显示有关此函数的摘要
%   此处显示详细说明
n = size(Matrix, 2)
S=Matrix(:,2:n)
Result=PCA(S)
plot(Result(:,1), Result(:,2), shape)
end

