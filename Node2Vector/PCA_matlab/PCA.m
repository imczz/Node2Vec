function [ R ] = PCA( S )
%UNTITLED 主成分分析降维成2维
%   此处显示详细说明
S1=S-repmat(mean(S),size(S,1),1);
C=cov(S1);
[P,L]=eig(C);
max1 = L(1, 1);
max2 = L(2, 2);
m1i = 1;
m2i = 2;
temp = 0;
if(max1 < max2)
    temp = max1;
    max1 = max2;
    max2 = temp;
    temp = m1i;
    m1i = m2i;
    m2i = temp;
end
for i = 3: size(L,1)
    temp = L(i, i);
    if(temp > max1)
        max2 = max1;
        max1 = temp;
        m2i = m1i;
        m1i = i;
    end
end
%R=zero(size(L,1), 2);
Q(:, 1) = P(:, m1i);
Q(:, 2) = P(:, m2i);
R=S1*Q
%plot(R(:,1),R(:,2),'go')
end

